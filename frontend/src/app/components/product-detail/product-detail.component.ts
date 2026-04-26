import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product!: Product;
  loading = true;
  error = '';
  showContactModal = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(Number(productId));
    }
  }

  loadProduct(id: number): void {
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
        this.loading = false;
      },
      error: (error) => {
        this.error = '商品不存在或已被删除';
        this.loading = false;
      }
    });
  }

  getProductImages(): string[] {
    if (this.product?.images) {
      const images = this.product.images.split(',').filter((img: string) => img.trim());
      return images.length > 0 ? images : ['https://picsum.photos/600/400'];
    }
    return ['https://picsum.photos/600/400'];
  }

  isOwner(): boolean {
    if (!this.authService.isLoggedIn() || !this.product) {
      return false;
    }
    return this.authService.getCurrentUserId() === this.product.sellerId;
  }

  getAuditStatusClass(): string {
    if (!this.product) return '';
    switch (this.product.auditStatus) {
      case 'PENDING': return 'badge-pending';
      case 'APPROVED': return 'badge-approved';
      case 'REJECTED': return 'badge-rejected';
      default: return '';
    }
  }

  getAuditStatusText(): string {
    if (!this.product) return '';
    switch (this.product.auditStatus) {
      case 'PENDING': return '待审核';
      case 'APPROVED': return '已通过';
      case 'REJECTED': return '已拒绝';
      default: return '';
    }
  }

  contactSeller(): void {
    if (this.isOwner()) {
      return;
    }
    this.showContactModal = true;
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  editProduct(): void {
    this.router.navigate(['/products', this.product.id, 'edit']);
  }
}
