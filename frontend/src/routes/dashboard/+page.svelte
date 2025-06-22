<script lang="ts">
	import { Navbar } from '$components';
	import { Dashboard } from '$components';
	import type { PageData } from './$types';
	import SuccessModal from '$components/other/SuccessModal.svelte';

	export let data: PageData;
	$: activeUser = data.activeUser;
	$: clientId = data.clientId;
</script>

<div class="main">
	<div class="start">
		<Navbar activeUser={activeUser} />
	</div>
	<SuccessModal/>
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