import apiClient from '$lib/config/axios.config';

export class LessonRepository {
	async getLessonsForClient(clientId: string): Promise<any[]> {
		try {
			const response = await apiClient.post('/lessons', null, {
				params: { clientId }
			});
			return response.data as any[];
		} catch (error: any) {
			throw error?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async getLessons(clientId: string) : Promise<any[]> {
		try {
			const response = await apiClient.post('/lessons/planned', null , {
				params: { clientId }
			});
			return response.data as any[];
		} catch (error: any) {
			throw error?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async getFinishedLessons(clientId: string) : Promise<any[]> {
		try {
			const response = await apiClient.post('/lessons/finished', null , {
				params: { clientId }
			});
			return response.data as any[];
		} catch (error: any) {
			throw error?.response?.data || 'An error occurred while fetching lessons';
		}
	}

	async enrollLesson(lessonId: string, clientId: string): Promise<boolean> {
		try {
			await apiClient.post('/lessons/enroll', null, {
				params: { lessonId, clientId }
			});
			return true;
		} catch (error: any) {
			if (error.response?.status === 404) {
				throw 'Lesson or client not found';
			}
			throw 'An error occurred while enrolling in the lesson';
		}
	}
}