<script lang="ts">
	import { Lesson } from '$components';
</script>


<div class="dashboard">
	<div class="lessons">
		<Lesson />
	</div>
</div>


<style lang="scss">
  .lessons {
		height: 500px;
    padding: 10px 0 10px 10px;
    border-left: 1px solid #000;
  }
</style>