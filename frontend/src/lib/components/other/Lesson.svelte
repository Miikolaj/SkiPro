<script lang="ts">
	import { Button } from '$lib/components';
	import { faStopwatch, faCalendarDays } from '@fortawesome/free-solid-svg-icons';
	import { Fa } from 'svelte-fa';

	export let date: string = 'placeholder';
	export let duration: string = 'placeholder';
	export let id: string = 'placeholder';
	export let enrolledClients: string = '0';
	export let firstName: string = 'placeholder';
	export let lastName: string = 'placeholder';
	export let qualificationLevel: string = 'placeholder';
	export let rating: string = 'placeholder';
	let maxClients: string = '5';

	$: progress = `${enrolledClients}/${maxClients}`;
</script>

<div class="lesson-card">
	<div class="top-section">
		<div class="lesson-title">Lesson #{id.slice(-3)}</div>
		<div class="lesson-description">
			<div class="icons">
				<Fa icon={faCalendarDays} size="0.75x" /> {date}
			</div>
			<div class="icons">
				<Fa icon={faStopwatch} size="0.75x" />{duration}
			</div>
		</div>
	</div>
	<div class="bottom-section">
		<div class="left-bottom">
			<div><strong>Instructor:</strong> {firstName} {lastName}</div>
			<div class="qualification">Qualification level: {qualificationLevel}</div>
			<div class="rating">Rating: {rating}/5 ⭐</div>
		</div>

		<div class="right-bottom">
			<div class="count">{progress}</div>
			<div class="spacer" />
			<Button type="lesson-tile">Enroll</Button>
		</div>
	</div>
</div>

<style lang="scss">
  .lesson-card {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
    padding: 10px;
    border: 1px solid #000;
    border-radius: 5px;
    font-weight: 300;
  }

  .top-section {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 0;

    .lesson-title {
      font-size: 1rem;
      margin: 0;
    }

    .lesson-description {
      display: flex;
      gap: 15px
    }

		.icons{
			display: flex;
			align-items: center;
			gap: 5px
		}
  }

  .bottom-section {
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .right-bottom {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      gap: 0.5rem;

      .spacer {
        flex: 1;
      }
    }

    .left-bottom {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      gap: 10px;
      font-size: 0.875rem;
    }
  }
</style>
