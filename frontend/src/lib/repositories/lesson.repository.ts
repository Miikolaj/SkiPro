import type { AxiosError } from 'axios';
import apiClient from '$lib/config/axios.config';

export type InstructorDTO = {
	firstName: string;
	lastName: string;
	id: string;
	qualificationLevel: string;
	rating: number;
};

export type LessonTileDTO = {
	id: string;
	date: string;
	duration: string;
	status: string;
	instructor: InstructorDTO;
	clientsCount: number;
	capacity: number;
};

export type ClientDTO = {
	firstName: string;
	lastName: string;
	id: string;
};

type SpringErrorBody =
	| string
	| {
			message?: string;
			error?: string;
			status?: number;
			path?: string;
			timestamp?: string;
	  };

function getErrorMessage(error: unknown, fallback: string): string {
	const err = error as AxiosError<SpringErrorBody> | undefined;

	if (!err?.response) return fallback;

	const data = err.response.data;

	if (typeof data === 'string' && data.trim().length > 0) return data;

	if (data && typeof data === 'object') {
		if (typeof data.message === 'string' && data.message.trim().length > 0) return data.message;
		if (typeof data.error === 'string' && data.error.trim().length > 0) return data.error;
	}

	switch (err.response.status) {
		case 400:
			return 'Bad request';
		case 401:
			return 'Unauthorized';
		case 403:
			return 'Forbidden';
		case 404:
			return 'Not found';
		case 409:
			return 'Conflict';
		case 500:
			return 'Server error';
		default:
			return fallback;
	}
}

export class LessonRepository {
	async getLessonsForClient(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons', null, { params: { clientId } });
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while fetching enrolled lessons');
		}
	}

	async getLessons(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons/planned', null, { params: { clientId } });
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while fetching available lessons');
		}
	}

	async getFinishedLessons(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons/finished', null, { params: { clientId } });
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while fetching finished lessons');
		}
	}

	async enrollLesson(lessonId: string, clientId: string): Promise<void> {
		try {
			await apiClient.post('/lessons/enroll', null, { params: { lessonId, clientId } });
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while enrolling in the lesson');
		}
	}

	async cancelEnrollment(lessonId: string, clientId: string): Promise<void> {
		try {
			await apiClient.post('/lessons/remove', null, { params: { lessonId, clientId } });
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while canceling the enrollment');
		}
	}

	async getLessonClients(lessonId: string): Promise<ClientDTO[]> {
		try {
			const response = await apiClient.get(`/lessons/${lessonId}/clients`);
			return response.data as ClientDTO[];
		} catch (error: unknown) {
			throw getErrorMessage(error, 'An error occurred while fetching lesson clients');
		}
	}
}
