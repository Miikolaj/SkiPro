import apiClient from '$lib/config/axios.config';

type InstructorDTO = {
	firstName: string;
	lastName: string;
	id: string;
	qualificationLevel: string;
	rating: number;
};

type LessonTileDTO = {
	id: string;
	date: string;
	duration: string;
	status: string;
	instructor: InstructorDTO;
	clientsCount: number;
};

type ClientDTO = { firstName: string; lastName: string; id: string };

export class LessonRepository {
	async getLessonsForClient(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons', null, {
				params: { clientId }
			});
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			const err = error as { response?: { data?: string } };
			throw err?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async getLessons(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons/planned', null, {
				params: { clientId }
			});
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			const err = error as { response?: { data?: string } };
			throw err?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async getFinishedLessons(clientId: string): Promise<LessonTileDTO[]> {
		try {
			const response = await apiClient.post('/lessons/finished', null, {
				params: { clientId }
			});
			return response.data as LessonTileDTO[];
		} catch (error: unknown) {
			const err = error as { response?: { data?: string } };
			throw err?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async enrollLesson(lessonId: string, clientId: string): Promise<boolean> {
		try {
			await apiClient.post('/lessons/enroll', null, {
				params: { lessonId, clientId }
			});
			return true;
		} catch (error: unknown) {
			const err = error as { response?: { status?: number } };
			if (err.response?.status === 404) {
				throw 'Lesson or client not found';
			}
			throw 'An error occurred while enrolling in the lesson';
		}
	}

	async cancelEnrollment(lessonId: string, clientId: string) {
		try {
			await apiClient.post('/lessons/remove', null, {
				params: {
					lessonId,
					clientId
				}
			});
			return true;
		} catch (error: unknown) {
			const err = error as { response?: { status?: number } };
			if (err.response?.status === 404) {
				throw 'Lesson or client not found';
			}
			throw 'An error occurred while canceling the enrollment';
		}
	}

	async getLessonClients(lessonId: string): Promise<ClientDTO[]> {
		try {
			const response = await apiClient.get(`/lessons/${lessonId}/clients`);
			return response.data as ClientDTO[];
		} catch (error: unknown) {
			const err = error as { response?: { data?: string } };
			throw err?.response?.data || 'An error occurred while fetching lesson clients';
		}
	}
}