<script lang="ts">
	import { Navbar } from '$components';
	import { Dashboard } from '$components';

	let activeUser: string = 'John Doe';
</script>

<div class="main">
	<div class="start">
		<Navbar />
	</div>
	<div class="dashboard">
		<Dashboard activeUser={activeUser} />
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