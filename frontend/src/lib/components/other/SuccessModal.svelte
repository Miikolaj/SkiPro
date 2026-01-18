<script lang="ts">
	export let lessonNumber: string | number | undefined;
	export let message: string = 'Placeholder';
</script>

<div class="modal">
	<div>
		<span class="modal-message">{message}{#if lessonNumber !== undefined && lessonNumber !== ''} #{lessonNumber}{/if}</span>
	</div>
</div>

<style lang="scss">
  .modal {
    width: 100%;
    padding: 6px 10px;
    background: #5BB146;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 20px;
    position: relative;
  }

  .modal-message {
    font-weight: 300;
  }
</style>