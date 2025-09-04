<script lang="ts">
	import { Button } from '$lib/components';
	import { faArrowRightToBracket, faUser, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
	import { Fa } from 'svelte-fa';

	export let activeUser: string;

	function handleSignOut() {
		document.cookie = 'token=; Max-Age=0; path=/;';
		window.location.reload();
	}
</script>

<div class="navbar-contents">
	<div class="name">
		SKIPRO
	</div>
	{#if activeUser}
		<div class="personal-info">
			<Fa icon={faUser} />
			<span>{activeUser}</span>
		</div>
	{/if}

	<div class="buttons">
		{#if !activeUser}
			<Button type="action" suffixIcon={faArrowRightToBracket}>Sign in</Button>
		{:else}
			<Button type="action leave" suffixIcon={faArrowRightFromBracket} on:click={handleSignOut}>Sign out</Button>
		{/if}
	</div>
</div>

<style lang="scss">
  .navbar-contents {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    padding: 15px 50px;
    position: relative;
    gap: 10px;
  }

  .personal-info {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 6px 10px;
    background: $navbar-color;
    border-radius: 5px;
  }

  .name {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    font-family: $font-logo;
    font-size: 3rem;
  }

  .buttons {
    display: inline-flex;
    gap: 15px;
    word-wrap: break-word;
  }
</style>