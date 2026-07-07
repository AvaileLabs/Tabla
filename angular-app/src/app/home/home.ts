import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  selectedFiles = signal<File[]>([]);

  protected onFilesSelected(newFiles: FileList): void {
    if (!newFiles.length) return;

    this.selectedFiles.update((currentFiles) => [...currentFiles, ...Array.from(newFiles)]);
  }
}
