import { writable } from 'svelte/store';

export const successModal = writable<{ visible: boolean; lessonNumber?: string | number }>(
  { visible: false, lessonNumber: undefined }
);