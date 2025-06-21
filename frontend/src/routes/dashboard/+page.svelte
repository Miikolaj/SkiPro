<script>
	import { Navbar } from '$components';
	import { DashboardView } from '$components';
</script>

<div class="main">
	<div class="start">
		<Navbar />
	</div>
	<div class="dashboard">
			<DashboardView />
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