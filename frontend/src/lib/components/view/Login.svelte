<script lang="ts">
	import { Button } from '$lib/components';

	let email = '';
	let password = '';

	const handleSubmit = () => {
		// TODO: Implement login logic
		console.log({ email, password });
	};
</script>

<div class="login">
	<div class="title">Log in with full name and password</div>
	<div class="inputs">
		<input class="input gap" type="email" placeholder="e.g., john.doe" bind:value={email} required />
		<input class="input" type="password" placeholder="Enter your password" bind:value={password} required />
	</div>
	<a href="/#" class="forgot-password">I forgot my password</a>
	<Button type="login-page">Log in</Button>
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