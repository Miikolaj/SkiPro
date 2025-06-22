<script lang="ts">
	import { Navbar, Dashboard, Modal } from '$components';
	import type { PageData } from './$types';
	import { successModal } from '$lib/stores/successModal';
	import { onDestroy } from 'svelte';

	let modalState: { visible: boolean; lessonNumber?: string | number } | null = null;

	const unsubscribe = successModal.subscribe(value => {
		modalState = value;
	});
	onDestroy(unsubscribe);

	export let data: PageData;
	$: activeUser = data.activeUser;
	$: clientId = data.clientId;
</script>

<div class="main">
	<div class="start">
		<Navbar activeUser={activeUser} />
	</div>
	{#if modalState?.visible}
		<Modal lessonNumber={modalState.lessonNumber} />
	{/if}
	<div class="dashboard">
		<Dashboard activeUser={activeUser} clientId={clientId} />
	</div>
</div>

<style lang="scss">
  .dashboard {
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 150px;
  }
</style>