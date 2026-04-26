import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Product, Page } from '../../models/product.model';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  keyword = '';
  selectedCategory: number | null = null;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    this.route.queryParams.subscribe(params => {
      this.keyword = params['keyword'] || '';
      this.selectedCategory = params['category'] ? Number(params['category']) : null;
      this.currentPage = 0;
      this.loadProducts();
    });
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
    this.loading = true;
    
    if (this.keyword) {
      this.productService.searchProducts(this.keyword, this.currentPage, this.pageSize)
        .subscribe({
          next: (page) => this.handlePageResponse(page),
          error: (error) => this.handleError(error)
        });
    } else if (this.selectedCategory) {
      this.productService.getProductsByCategory(this.selectedCategory, this.currentPage, this.pageSize)
        .subscribe({
          next: (page) => this.handlePageResponse(page),
          error: (error) => this.handleError(error)
        });
    } else {
      this.productService.getApprovedProducts(this.currentPage, this.pageSize)
        .subscribe({
          next: (page) => this.handlePageResponse(page),
          error: (error) => this.handleError(error)
        });
    }
  }

  handlePageResponse(page: Page<Product>): void {
    this.products = page.content;
    this.totalPages = page.totalPages;
    this.totalElements = page.totalElements;
    this.loading = false;
  }

  handleError(error: any): void {
    console.error('Failed to load products', error);
    this.loading = false;
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadProducts();
  }

  goToProduct(productId: number): void {
    this.router.navigate(['/products', productId]);
  }

  filterByCategory(categoryId: number | null): void {
    this.selectedCategory = categoryId;
    this.keyword = '';
    this.currentPage = 0;
    
    if (categoryId) {
      this.router.navigate(['/products'], { queryParams: { category: categoryId } });
    } else {
      this.router.navigate(['/products']);
    }
  }

  search(): void {
    if (this.keyword.trim()) {
      this.selectedCategory = null;
      this.currentPage = 0;
      this.router.navigate(['/products'], { queryParams: { keyword: this.keyword } });
    }
  }

  getProductImage(product: Product): string {
    if (product.images) {
      const images = product.images.split(',');
      return images[0] || 'https://picsum.photos/400/300';
    }
    return 'https://picsum.photos/400/300';
  }

  get pages(): number[] {
    const pages: number[] = [];
    for (let i = 0; i < this.totalPages; i++) {
      pages.push(i);
    }
    return pages;
  }
}
