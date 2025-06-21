import type { PageServerLoad } from './$types';
import { redirect } from '@sveltejs/kit';
import { getSubFromToken } from '$lib/utils/jwt';

export const load: PageServerLoad = async ({ cookies }) => {
	const token = cookies.get('token');
	const sub = getSubFromToken(token);
	if (sub) {
		throw redirect(302, '/dashboard');
	}
};