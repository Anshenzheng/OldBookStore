import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard, AdminGuard, StudentGuard } from './guards/auth.guard';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { MyProductsComponent } from './components/my-products/my-products.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { 
    path: 'products/create', 
    component: CreateProductComponent,
    canActivate: [AuthGuard, StudentGuard]
  },
  { 
    path: 'products/:id/edit', 
    component: CreateProductComponent,
    canActivate: [AuthGuard, StudentGuard]
  },
  { 
    path: 'products/my', 
    component: MyProductsComponent,
    canActivate: [AuthGuard, StudentGuard]
  },
  { 
    path: 'admin', 
    component: AdminDashboardComponent,
    canActivate: [AuthGuard, AdminGuard]
  },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
