<script lang="ts">
	import { Button } from '$lib/components';
	import {OathRepository} from '$lib/repositories/oath.repository';
	import { getSubFromToken } from '$lib/utils/jwt';

	let fullName = '';
	let password = '';

	const oathRepository = new OathRepository();

	const handleSubmit = async () => {
		try {
			const isAuthenticated = await oathRepository.oath(fullName, password);
			if (isAuthenticated) {
				document.cookie = `token=${isAuthenticated}; path=/;`;
				const decoded: any = getSubFromToken(isAuthenticated);
				window.location.href = '/dashboard';
			} else {
				alert('Invalid credentials, please try again.');
			}
		} catch (error) {
			console.error('Authentication error:', error);
			alert('An error occurred during authentication. Please try again later.');
		}
	};
</script>

<div class="login">
	<div class="title">Log in with full name and password</div>
	<div class="inputs">
		<input class="input gap" type="email" placeholder="e.g., john.doe" bind:value={fullName} required />
		<input class="input" type="password" placeholder="Enter your password" bind:value={password} required />
	</div>
	<a href="/#" class="forgot-password">I forgot my password</a>
	<Button type="login-page" on:click={handleSubmit}>Log in</Button>
	<div class="signup">
		Don't have an account?
		<a href="#" class="signup-href">Sign up</a>
	</div>
</div>

<style lang="scss">
  .login {
    display: flex;
    flex-direction: column;
    max-width: 20rem;
  }

  .title {
    margin-bottom: 30px;
  }

  .gap {
    margin-bottom: 5px;
  }

  .input {
    padding: 10px;
    font-size: 0.875rem;
    border: 1px solid #d6d6d6;
    border-radius: 5px;
    width: 100%;
    margin-bottom: 10px;

    &::placeholder {
      color: #d6d6d6;
    }
  }

  .forgot-password {
    text-align: center;
    font-size: 0.75rem;
    color: #4c4c4c;
    text-decoration: underline;
    margin-bottom: 30px;
  }

  .signup {
    font-size: 0.75rem;
    text-align: center;
  }

  .signup-href {
    text-decoration: underline;
  }
</style>