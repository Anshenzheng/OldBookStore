import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, CreateProductRequest, Page } from '../models/product.model';

const API_URL = 'http://localhost:8080/api/products/';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  getApprovedProducts(page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + 'public/list', { params });
  }

  searchProducts(keyword: string, page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + 'public/search', { params });
  }

  getProductsByCategory(categoryId: number, page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + `public/category/${categoryId}`, { params });
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(API_URL + `public/${id}`);
  }

  getMyProducts(page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + 'my', { params });
  }

  createProduct(request: CreateProductRequest): Observable<Product> {
    return this.http.post<Product>(API_URL, request);
  }

  updateProduct(id: number, request: CreateProductRequest): Observable<Product> {
    return this.http.put<Product>(API_URL + id, request);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(API_URL + id);
  }
}
