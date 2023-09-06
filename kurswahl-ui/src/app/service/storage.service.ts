import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  public static saveData(key: string, value: string) {
    sessionStorage.setItem(key, value);
  }

  public static getData(key: string) {
    return sessionStorage.getItem(key)
  }
  public static removeData(key: string) {
    sessionStorage.removeItem(key);
  }

  public static clearData() {
    sessionStorage.clear();
  }
}
