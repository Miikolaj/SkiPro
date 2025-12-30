import { writable } from 'svelte/store';

export type SuccessModalState = {
    visible: boolean;
    lessonNumber?: string | number;
    message?: string;
};

export const successModal = writable<SuccessModalState>({
    visible: false,
    lessonNumber: undefined,
    message: undefined
});