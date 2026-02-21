import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UploadResponse {
  success: boolean;
  message: string;
  fileUrl: string;
  fileName: string;
}

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiUrl = `${environment.apiUrl}/upload`;

  constructor(private http: HttpClient) {}

  /**
   * Upload d'un fichier unique
   */
  uploadFile(file: File, type: 'cv' | 'cv-cnfcpp' | 'photo'): Observable<UploadResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.http.post<UploadResponse>(`${this.apiUrl}`, formData);
  }

  /**
   * Upload avec progression
   */
  uploadFileWithProgress(file: File, type: string): Observable<number | UploadResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.http.post<UploadResponse>(this.apiUrl, formData, {
      reportProgress: true,
      observe: 'events'
    }).pipe(
      map((event: HttpEvent<any>) => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            const progress = event.total ? Math.round((100 * event.loaded) / event.total) : 0;
            return progress;
          case HttpEventType.Response:
            return event.body;
          default:
            return 0;
        }
      })
    );
  }

  /**
   * Validation de fichier
   */
  validateFile(file: File, type: 'cv' | 'photo'): { valid: boolean; error?: string } {
    const maxSize = type === 'photo' ? 5 * 1024 * 1024 : 10 * 1024 * 1024; // 5MB pour photo, 10MB pour CV

    const allowedTypes = type === 'photo'
      ? ['image/jpeg', 'image/jpg', 'image/png', 'image/webp']
      : ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];

    if (file.size > maxSize) {
      return {
        valid: false,
        error: `Le fichier doit faire moins de ${maxSize / (1024 * 1024)}MB`
      };
    }

    if (!allowedTypes.includes(file.type)) {
      return {
        valid: false,
        error: type === 'photo'
          ? 'Format accepté: JPG, PNG, WEBP'
          : 'Format accepté: PDF, DOC, DOCX'
      };
    }

    return { valid: true };
  }
}
