import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent implements OnInit {
  productForm!: FormGroup;
  categories: Category[] = [];
  loading = false;
  submitted = false;
  error = '';
  success = '';
  isEditMode = false;
  productId: number | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private productService: ProductService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    this.productForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(2000)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      images: [''],
      categoryId: [''],
      contactInfo: ['', [Validators.maxLength(100)]]
    });

    this.loadCategories();

    const editId = this.route.snapshot.paramMap.get('id');
    if (editId) {
      this.isEditMode = true;
      this.productId = Number(editId);
      this.loadProductForEdit();
    }
  }

  get f() { return this.productForm.controls; }

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

  loadProductForEdit(): void {
    if (!this.productId) return;
    
    this.loading = true;
    this.productService.getProductById(this.productId).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          title: product.title,
          description: product.description,
          price: product.price,
          images: product.images,
          categoryId: product.categoryId,
          contactInfo: product.contactInfo
        });
        this.loading = false;
      },
      error: (error) => {
        this.error = '商品不存在或无权编辑';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.productForm.invalid) {
      return;
    }

    this.loading = true;
    
    if (this.isEditMode && this.productId) {
      this.productService.updateProduct(this.productId, this.productForm.value)
        .subscribe({
          next: () => {
            this.success = '商品更新成功！';
            this.loading = false;
            setTimeout(() => {
              this.router.navigate(['/products/my']);
            }, 1500);
          },
          error: (error: any) => {
            this.error = error.error?.message || '更新失败，请重试';
            this.loading = false;
          }
        });
    } else {
      this.productService.createProduct(this.productForm.value)
        .subscribe({
          next: () => {
            this.success = '商品发布成功！等待管理员审核后即可显示。';
            this.loading = false;
            setTimeout(() => {
              this.router.navigate(['/products/my']);
            }, 2000);
          },
          error: (error: any) => {
            this.error = error.error?.message || '发布失败，请重试';
            this.loading = false;
          }
        });
    }
  }

  cancel(): void {
    this.router.navigate(['/products/my']);
  }
}
