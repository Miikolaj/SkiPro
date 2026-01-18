<script lang="ts">
	import { Navbar, Dashboard, Modal } from '$components';
	import type { PageData } from './$types';
	import { successModal } from '$lib/stores/successModal';
	import { onDestroy, onMount } from 'svelte';

	let modalState: { visible: boolean; lessonNumber?: string | number; message?: string } | null = null;
	let modalTimeout: ReturnType<typeof setTimeout> | null = null;

	const unsubscribe = successModal.subscribe(value => {
		modalState = value;
		if (modalState?.visible) {
			if (modalTimeout) clearTimeout(modalTimeout);
			modalTimeout = setTimeout(() => {
				successModal.set({ visible: false, lessonNumber: undefined, message: undefined });
			}, 5000);
		} else {
			if (modalTimeout) clearTimeout(modalTimeout);
		}
	});

	onMount(() => {
		const lessonNumber = localStorage.getItem('showSuccessModal');
		const lessonCancel = localStorage.getItem('showCancelModal');
		if (lessonNumber) {
			successModal.set({ visible: true, lessonNumber, message: 'You are enrolled in lesson' });
			localStorage.removeItem('showSuccessModal');
		} else if (lessonCancel) {
			successModal.set({ visible: true, lessonNumber: lessonCancel, message: 'Enrollment canceled for lesson' });
			localStorage.removeItem('showCancelModal');
		}
	});

	onDestroy(() => {
		unsubscribe();
		clearTimeout(modalTimeout!);
	});

	export let data: PageData;
	let activeUser: string = '';
	let clientId: string = '';
	$: activeUser = String(data.activeUser ?? '');
	$: clientId = String(data.clientId ?? '');
</script>

<div class="main">
	<div class="start">
		<Navbar activeUser={activeUser} />
	</div>
	{#if modalState?.visible}
		<div class="modal-overlay">
			<Modal lessonNumber={modalState.lessonNumber ?? ''} message={modalState.message ?? ''} />
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