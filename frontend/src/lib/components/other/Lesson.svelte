<script lang="ts">
	import { Button } from '$lib/components';
	import { faStopwatch, faCalendarDays, faStar } from '@fortawesome/free-solid-svg-icons';
	import { Fa } from 'svelte-fa';
	import { LessonRepository } from '$lib/repositories/lesson.repository';

	const lessonRepository = new LessonRepository();

	export let clients: { firstName: string; lastName: string; id: string }[] = [];
	export let currentUser: string = 'placeholder';
	export let date: string = 'placeholder';
	export let duration: string = 'placeholder';
	export let id: string = 'placeholder';
	export let enrolledClients: string = 'placeholder';
	export let firstName: string = 'placeholder';
	export let lastName: string = 'placeholder';
	export let qualificationLevel: string = 'placeholder';
	export let rating: string = 'placeholder';
	export let section: string = 'available';
	let maxClients: string = '5';

	function handleEnroll() {
		lessonRepository.enrollLesson(id, currentUser);
		localStorage.setItem('showSuccessModal', id.slice(-3));
		setTimeout(() => {
			location.reload();
		}, 100);
	}

	function handleCancel() {
		lessonRepository.cancelEnrollment(id, currentUser);
		localStorage.setItem('showCancelModal', id.slice(-3));
		setTimeout(() => {
			location.reload();
		}, 100);
	}

	$: uniqueClients = Array.from(new Map(clients.map(c => [c.id, c])).values());
	$: console.log(uniqueClients);
	$: progress = `${enrolledClients}/${maxClients}`;
	$: buttonLabel =
		section === 'available'
			? 'Enroll'
			: section === 'enrolled'
				? 'Cancel Participation'
				: section === 'finished'
					? 'Rate Instructor'
					: 'Action';
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
			<div class="qualification">Qualification : {qualificationLevel}</div>
			<div class="rating">Rating: {rating}/5
				<Fa icon={faStar} />
			</div>
		</div>

		<div class="right-bottom">
			<div class="count">
				<span class="left">Enrolled Clients: </span>
				<span class="right">{progress}</span>
			</div>
			{#if uniqueClients.length > 0}
				{#each uniqueClients as c}
					<div class="client">{c.firstName} {c.lastName}</div>
				{/each}
			{/if}
			<div class="spacer" />
			<div class="enroll-wrapper">
				<Button type="lesson-tile" on:click={section === 'available' ? handleEnroll : section === 'enrolled' ? handleCancel : handleEnroll}
								disabled={section !== 'available'}>{buttonLabel}</Button>
			</div>
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

    .icons {
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
      align-items: flex-start;
      gap: 0.5rem;
			min-width: 145px;


      .spacer {
        flex: 1;
      }

      .count {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 100%;
      }

      .enroll-wrapper {
        width: 100%;
        display: flex;
        justify-content: flex-end; // pushes button to the right
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
