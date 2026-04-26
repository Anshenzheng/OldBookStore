import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Product, Page } from '../../models/product.model';

@Component({
  selector: 'app-admin-audit',
  templateUrl: './admin-audit.component.html',
  styleUrls: ['./admin-audit.component.css']
})
export class AdminAuditComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;
  showRejectModal = false;
  selectedProduct: Product | null = null;
  rejectReason = '';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadPendingProducts();
  }

  loadPendingProducts(): void {
    this.loading = true;
    this.adminService.getPendingProducts(this.currentPage, this.pageSize).subscribe({
      next: (page: Page<Product>) => {
        this.products = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load pending products', error);
        this.loading = false;
      }
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadPendingProducts();
  }

  approveProduct(product: Product): void {
    if (confirm(`确定要审核通过商品 "${product.title}" 吗？`)) {
      this.adminService.approveProduct(product.id).subscribe({
        next: () => {
          this.loadPendingProducts();
        },
        error: (error) => {
          console.error('Failed to approve product', error);
          alert('审核失败，请重试');
        }
      });
    }
  }

  openRejectModal(product: Product): void {
    this.selectedProduct = product;
    this.rejectReason = '';
    this.showRejectModal = true;
  }

  rejectProduct(): void {
    if (!this.selectedProduct) return;
    
    const reason = this.rejectReason || '商品不符合发布规范';
    
    this.adminService.rejectProduct(this.selectedProduct.id, reason).subscribe({
      next: () => {
        this.showRejectModal = false;
        this.selectedProduct = null;
        this.loadPendingProducts();
      },
      error: (error) => {
        console.error('Failed to reject product', error);
        alert('操作失败，请重试');
      }
    });
  }

  getProductImage(product: Product): string {
    if (product.images) {
      const images = product.images.split(',');
      return images[0] || 'https://picsum.photos/200/150';
    }
    return 'https://picsum.photos/200/150';
  }

  get pages(): number[] {
    const pages: number[] = [];
    for (let i = 0; i < this.totalPages; i++) {
      pages.push(i);
    }
    return pages;
  }
}
