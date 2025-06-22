import type { PageServerLoad } from './$types';
import { redirect } from '@sveltejs/kit';
import { getSubFromToken, getClientIdFromToken } from '$lib/utils/jwt';

export const load: PageServerLoad = async ({ cookies }) => {
	const token = cookies.get('token');
	const sub = getSubFromToken(token);
	const clientId = getClientIdFromToken(token);
	if (!sub) {
		throw redirect(302, '/');
	}
	return { activeUser: sub, clientId: clientId };
};
