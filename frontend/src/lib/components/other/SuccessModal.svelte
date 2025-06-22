<script lang="ts">
	import { Button } from '$components';
	import { faXmark } from '@fortawesome/free-solid-svg-icons';

	export let lessonNumber: string | number;
	export let message: string = 'Successfully enrolled for Lesson';
</script>

<div class="modal">
	<div>
		<span class="modal-message">{message} #{lessonNumber}</span>
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
    align-items: center;
    gap: 20px;
    position: relative;
  }

  .modal-message {
    font-weight: 300;
  }
</style>