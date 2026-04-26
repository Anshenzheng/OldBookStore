export interface Product {
  id: number;
  title: string;
  description?: string;
  price: number;
  images?: string;
  categoryId?: number;
  categoryName?: string;
  sellerId: number;
  sellerNickname?: string;
  sellerAvatar?: string;
  status: string;
  auditStatus: string;
  rejectReason?: string;
  createdAt: Date;
  viewCount?: number;
  contactInfo?: string;
}

export interface CreateProductRequest {
  title: string;
  description: string;
  price: number;
  images: string;
  categoryId: number;
  contactInfo: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
