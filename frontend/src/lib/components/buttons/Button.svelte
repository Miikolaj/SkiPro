<script lang="ts">
	import type { IconDefinition } from '@fortawesome/fontawesome-common-types';
	import { Fa, type IconSize } from 'svelte-fa';

	export let suffixIcon: IconDefinition | undefined = undefined;
	export let prefixIcon: IconDefinition | undefined = undefined;
	export let href: string = '#';
	export let type: string = '';
	export let size: IconSize = '1x';
</script>

<a
	{href}
	class="button {type}"
	on:click|preventDefault
>
	{#if prefixIcon}
		<Fa icon={prefixIcon} {size} />
	{/if}
	<slot />
	{#if suffixIcon}
		<Fa icon={suffixIcon} {size} />
	{/if}
</a>

<style lang="scss">
	.button{
		display: inline-flex;
		gap : 10px;
		align-items: center;
	}

	.action, .start-button{
		padding: 6px 10px;
		background-color: $navbar-color;
    backdrop-filter: blur(2px);
    border-radius: 5px;
	}

	.start-button{
		font-size: 1.25rem;
		padding: 6px 15px;
	}
</style>