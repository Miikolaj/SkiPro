import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:8080',
				changeOrigin: true,
				rewrite: (path) => path.replace(/^\/api/, '')
			}
		}
	},
	css: {
		preprocessorOptions: {
			// if using SCSS
			scss: {
				additionalData: `
                @use './src/styles/main' as *;
            `,
			},
		},
	}
});
