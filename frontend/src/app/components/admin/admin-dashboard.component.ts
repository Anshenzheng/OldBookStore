import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  statistics: any = {};
  loading = true;
  activeTab = 'overview';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.adminService.getStatistics().subscribe({
      next: (stats) => {
        this.statistics = stats;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load statistics', error);
        this.loading = false;
      }
    });
  }

  exportProducts(): void {
    this.adminService.exportProducts().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `products_${new Date().toISOString().split('T')[0]}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Failed to export products', error);
        alert('导出失败，请重试');
      }
    });
  }

  exportUsers(): void {
    this.adminService.exportUsers().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `users_${new Date().toISOString().split('T')[0]}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Failed to export users', error);
        alert('导出失败，请重试');
      }
    });
  }
}
