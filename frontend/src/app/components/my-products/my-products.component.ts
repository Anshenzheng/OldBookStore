import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product, Page } from '../../models/product.model';

@Component({
  selector: 'app-my-products',
  templateUrl: './my-products.component.html',
  styleUrls: ['./my-products.component.css']
})
export class MyProductsComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private productService: ProductService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getMyProducts(this.currentPage, this.pageSize).subscribe({
      next: (page: Page<Product>) => {
        this.products = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load my products', error);
        this.loading = false;
      }
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadProducts();
  }

  goToProduct(productId: number): void {
    this.router.navigate(['/products', productId]);
  }

  editProduct(productId: number): void {
    this.router.navigate(['/products', productId, 'edit']);
  }

  deleteProduct(product: Product): void {
    if (confirm(`确定要删除商品 "${product.title}" 吗？`)) {
      this.productService.deleteProduct(product.id).subscribe({
        next: () => {
          this.loadProducts();
        },
        error: (error) => {
          console.error('Failed to delete product', error);
          alert('删除失败，请重试');
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
