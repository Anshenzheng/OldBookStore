import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, Page } from '../models/product.model';
import { User } from '../models/user.model';
import { Category } from '../models/category.model';

const API_URL = 'http://localhost:8080/api/admin/';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getStatistics(): Observable<any> {
    return this.http.get<any>(API_URL + 'statistics');
  }

  getPendingProducts(page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + 'products/pending', { params });
  }

  getAllProducts(page: number = 0, size: number = 10): Observable<Page<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Product>>(API_URL + 'products', { params });
  }

  approveProduct(id: number): Observable<Product> {
    return this.http.post<Product>(API_URL + `products/${id}/approve`, {});
  }

  rejectProduct(id: number, reason: string): Observable<Product> {
    return this.http.post<Product>(API_URL + `products/${id}/reject`, { reason });
  }

  takeProductOffShelf(id: number): Observable<Product> {
    return this.http.post<Product>(API_URL + `products/${id}/off-shelf`, {});
  }

  getAllUsers(page: number = 0, size: number = 10): Observable<Page<User>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<User>>(API_URL + 'users', { params });
  }

  banUser(id: number): Observable<User> {
    return this.http.post<User>(API_URL + `users/${id}/ban`, {});
  }

  unbanUser(id: number): Observable<User> {
    return this.http.post<User>(API_URL + `users/${id}/unban`, {});
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(API_URL + 'categories');
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(API_URL + 'categories', category);
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(API_URL + `categories/${id}`, category);
  }

  deleteCategory(id: number): Observable<any> {
    return this.http.delete(API_URL + `categories/${id}`);
  }

  exportProducts(): Observable<Blob> {
    return this.http.get(API_URL + 'export/products', {
      responseType: 'blob'
    });
  }

  exportUsers(): Observable<Blob> {
    return this.http.get(API_URL + 'export/users', {
      responseType: 'blob'
    });
  }
}
