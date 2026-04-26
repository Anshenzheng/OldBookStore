import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = true;
  searchKeyword = '';

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadProducts();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Failed to load categories', error);
      }
    });
  }

  loadProducts(): void {
    this.productService.getApprovedProducts(0, 8).subscribe({
      next: (page) => {
        this.products = page.content;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load products', error);
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchKeyword.trim()) {
      this.router.navigate(['/products'], { queryParams: { keyword: this.searchKeyword } });
    }
  }

  goToCategory(categoryId: number): void {
    this.router.navigate(['/products'], { queryParams: { category: categoryId } });
  }

  goToProduct(productId: number): void {
    this.router.navigate(['/products', productId]);
  }

  getProductImage(product: Product): string {
    if (product.images) {
      const images = product.images.split(',');
      return images[0] || 'https://picsum.photos/400/300';
    }
    return 'https://picsum.photos/400/300';
  }
}
