<script lang="ts">
	import type { IconDefinition } from '@fortawesome/fontawesome-common-types';
	import { Fa, type IconSize } from 'svelte-fa';

	export let suffixIcon: IconDefinition | undefined = undefined;
	export let prefixIcon: IconDefinition | undefined = undefined;
	// if href is provided (non-empty), render <a>, otherwise render <button>
	export let href: string | undefined = undefined;
	export let type: string = '';
	export let size: IconSize = '1x';
	export let disabled: string | boolean | undefined = undefined;

	$: isDisabled = disabled === true || disabled === 'true';
	$: classes = `button ${type}`.trim();

	function handleAnchorClick(event: MouseEvent) {
		if (isDisabled) {
			event.preventDefault();
			event.stopPropagation();
		}
	}
</script>

{#if href}
	<a
		class={classes}
		href={href}
		aria-disabled={isDisabled ? 'true' : undefined}
		on:click={handleAnchorClick}
	>
		{#if prefixIcon}
			<Fa icon={prefixIcon} {size} />
		{/if}
		<slot />
		{#if suffixIcon}
			<Fa icon={suffixIcon} {size} />
		{/if}
	</a>
{:else}
	<button class={classes} on:click disabled={isDisabled}>
		{#if prefixIcon}
			<Fa icon={prefixIcon} {size} />
		{/if}
		<slot />
		{#if suffixIcon}
			<Fa icon={suffixIcon} {size} />
		{/if}
	</button>
{/if}

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

	.login-page {
    background: #0086bf;
    color: #fff;
    font-size: 0.875rem;
    padding: 0.625rem 0;
    border-radius: 5px;
    cursor: pointer;
    justify-content: center;
    margin-bottom: 20px;

    &:hover {
      background: darken(#0086bf, 5%);
    }
	}

	.lesson-tile{
    align-self: flex-start;
    padding: 0.3125rem 0.625rem;
    background: rgba(58, 156, 198, 0.5);
    border: none;
    border-radius: 5px;
    font-size: 0.875rem;
    cursor: pointer;

    &:hover {
      background: rgba(58, 156, 198, 0.7);
    }
	}

  .dimmed {
    opacity: 0.5;
    pointer-events: none;
  }

  .lesson-tile.inactive {
    background:  #D32F2F;
		color: #F5F5F5;
  }

	.active {
    background: rgba(0, 134, 191, 0.60);
		padding: 5px;
		border-radius: 5px;
	}

	.inactive {
		padding: 5px;
		border-radius: 5px;
	}

	.sign-out{
		font-weight: 300;
		color: #FF3D3D;
		padding: 5px;
	}

	.leave{
    background: rgba(137.31, 7.26, 9.43, 0.50);
    backdrop-filter: blur(2px);
    border-radius: 5px;
	}
</style>