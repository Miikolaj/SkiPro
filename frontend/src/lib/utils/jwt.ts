export function getSubFromToken(token: string | undefined): string | null {
	if (!token) return null;
	const parts = token.split('.');
	if (parts.length !== 3) return null;
	try {
		const payload = JSON.parse(Buffer.from(parts[1], 'base64url').toString());
		return payload.sub || null;
	} catch {
		return null;
	}
}