<script lang="ts">
	import { Navbar } from '$components';
	import Login from '$components/view/Login.svelte';
</script>

<div class="main">
	<div class="start">
		<Navbar />
	</div>
	<div class="login">
		<Login/>
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