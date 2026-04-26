import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { User } from '../../models/user.model';
import { Page } from '../../models/product.model';

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  users: User[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadAllUsers();
  }

  loadAllUsers(): void {
    this.loading = true;
    this.adminService.getAllUsers(this.currentPage, this.pageSize).subscribe({
      next: (page: Page<User>) => {
        this.users = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load users', error);
        this.loading = false;
      }
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadAllUsers();
  }

  banUser(user: User): void {
    if (confirm(`确定要禁用用户 "${user.nickname}" 吗？`)) {
      this.adminService.banUser(user.id).subscribe({
        next: () => {
          this.loadAllUsers();
        },
        error: (error) => {
          console.error('Failed to ban user', error);
          alert('操作失败，请重试');
        }
      });
    }
  }

  unbanUser(user: User): void {
    if (confirm(`确定要解禁用户 "${user.nickname}" 吗？`)) {
      this.adminService.unbanUser(user.id).subscribe({
        next: () => {
          this.loadAllUsers();
        },
        error: (error) => {
          console.error('Failed to unban user', error);
          alert('操作失败，请重试');
        }
      });
    }
  }

  getUserAvatar(user: User): string {
    return user.avatar || 'https://picsum.photos/50/50';
  }

  getRoleClass(role: string): string {
    switch (role) {
      case 'ADMIN': return 'badge bg-danger';
      case 'STUDENT': return 'badge bg-primary';
      default: return 'badge bg-secondary';
    }
  }

  getRoleText(role: string): string {
    switch (role) {
      case 'ADMIN': return '管理员';
      case 'STUDENT': return '学生';
      default: return role;
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'badge bg-success';
      case 'BANNED': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'ACTIVE': return '正常';
      case 'BANNED': return '禁用';
      default: return status;
    }
  }

  get pages(): number[] {
    const pages: number[] = [];
    for (let i = 0; i < this.totalPages; i++) {
      pages.push(i);
    }
    return pages;
  }
}
