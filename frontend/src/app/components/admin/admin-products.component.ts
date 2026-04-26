import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Product, Page } from '../../models/product.model';

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.component.html',
  styleUrls: ['./admin-products.component.css']
})
export class AdminProductsComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadAllProducts();
  }

  loadAllProducts(): void {
    this.loading = true;
    this.adminService.getAllProducts(this.currentPage, this.pageSize).subscribe({
      next: (page: Page<Product>) => {
        this.products = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load products', error);
        this.loading = false;
      }
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadAllProducts();
  }

  takeOffShelf(product: Product): void {
    if (confirm(`确定要将商品 "${product.title}" 下架吗？`)) {
      this.adminService.takeProductOffShelf(product.id).subscribe({
        next: () => {
          this.loadAllProducts();
        },
        error: (error) => {
          console.error('Failed to take off shelf', error);
          alert('操作失败，请重试');
        }
      });
    }
  }

  getProductImage(product: Product): string {
    if (product.images) {
      const images = product.images.split(',');
      return images[0] || 'https://picsum.photos/200/150';
    }
    return 'https://picsum.photos/200/150';
  }

  getAuditStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'badge-pending';
      case 'APPROVED': return 'badge-approved';
      case 'REJECTED': return 'badge-rejected';
      default: return '';
    }
  }

  getAuditStatusText(status: string): string {
    switch (status) {
      case 'PENDING': return '待审核';
      case 'APPROVED': return '已通过';
      case 'REJECTED': return '已拒绝';
      default: return '';
    }
  }

  getProductStatusClass(status: string): string {
    switch (status) {
      case 'ON_SALE': return 'badge bg-success';
      case 'SOLD': return 'badge bg-secondary';
      case 'OFF_SHELF': return 'badge bg-secondary';
      default: return '';
    }
  }

  getProductStatusText(status: string): string {
    switch (status) {
      case 'ON_SALE': return '在售';
      case 'SOLD': return '已售出';
      case 'OFF_SHELF': return '已下架';
      default: return '';
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
