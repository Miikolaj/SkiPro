<script lang="ts">
	import { Navbar, Dashboard, Modal } from '$components';
	import type { PageData } from './$types';
	import { successModal } from '$lib/stores/successModal';
	import { onDestroy, onMount } from 'svelte';

	let modalState: { visible: boolean; lessonNumber?: string | number } | null = null;
	let modalTimeout: ReturnType<typeof setTimeout> | null = null;

	const unsubscribe = successModal.subscribe(value => {
		modalState = value;
		if (modalState?.visible) {
			clearTimeout(modalTimeout!);
			modalTimeout = setTimeout(() => {
				successModal.set({ visible: false, lessonNumber: undefined });
			}, 5000);
		} else {
			clearTimeout(modalTimeout!);
		}
	});

	onMount(() => {
		const lessonNumber = localStorage.getItem('showSuccessModal');
		if (lessonNumber) {
			successModal.set({ visible: true, lessonNumber });
			localStorage.removeItem('showSuccessModal');
		}
	});

	onDestroy(() => {
		unsubscribe();
		clearTimeout(modalTimeout!);
	});

	export let data: PageData;
	$: activeUser = data.activeUser;
	$: clientId = data.clientId;
</script>

<div class="main">
	<div class="start">
		<Navbar activeUser={activeUser} />
	</div>
	{#if modalState?.visible}
		<div class="modal-overlay">
			<Modal lessonNumber={modalState.lessonNumber} />
		</div>
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

  .modal-overlay {
		width: 100%;
    position: fixed;
    top: 60px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1000;
  }
</style>