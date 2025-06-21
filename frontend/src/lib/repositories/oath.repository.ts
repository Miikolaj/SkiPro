import apiClient from '$lib/config/axios.config';

export class OathRepository {
	async oath(fullName: string, password: string): Promise<string> {
		try {
			const response = await apiClient.post('/auth/login',null, {
				params: { fullName, password }
			});
			return response.data as string;
		} catch (error: any) {
			throw error?.response?.data || 'An error occurred while authenticating';
		}
	}
}