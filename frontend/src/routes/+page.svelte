<script lang="ts">
	import { LoginView, Navbar } from '$components';
</script>

<div class="main">
	<div class="start">
		<Navbar />
	</div>
	<div class="login">
		<LoginView/>
	</div>
</div>

<style lang="scss">
	.login{
		display: flex;
		justify-content: center;
		align-items: center;
		padding-top: 150px;
	}
</style>