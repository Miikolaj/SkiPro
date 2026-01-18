<script lang="ts">
	import { onMount } from 'svelte';
	import { Button, Lesson } from '$components';
	import { faPersonSkiing, faList, faFlagCheckered, faRepeat, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
	import { LessonRepository } from '$lib/repositories/lesson.repository';

	type LessonDto = {
		id: string;
		date: string;
		duration: string;
		clientsCount: string;
		instructor: {
			firstName: string;
			lastName: string;
			qualificationLevel: string;
			rating: string;
		};
	};

	const lessonRepository = new LessonRepository();

	let loading = true;
	let activeSection: string = 'available';
	let enrolledLessons: LessonDto[] = [];
	let plannedLessons: LessonDto[] = [];
	let finishedLessons: LessonDto[] = [];
	export let clientId: string;
	export let activeUser: string;

	onMount(async () => {
		enrolledLessons = await lessonRepository.getLessonsForClient(clientId);
		plannedLessons = await lessonRepository.getLessons(clientId);
		finishedLessons = await lessonRepository.getFinishedLessons(clientId);
		loading = false;
	});

	const sections = [
		{ key: 'available', icon: faList, label: 'Available Lessons' },
		{ key: 'enrolled', icon: faPersonSkiing, label: 'Enrolled Lessons' },
		{ key: 'finished', icon: faFlagCheckered, label: 'Finished Lessons' },
		{ key: 'rentals', icon: faRepeat, label: 'Rentals' },
		{ key: 'signout', icon: faArrowRightFromBracket, label: 'Sign Out', type: 'sign-out' }
	];

	function handleSignOut() {
		document.cookie = 'token=; Max-Age=0; path=/;';
		window.location.reload();
	}

	function setActiveSection(next: string) {
		if (next === activeSection) return;
		activeSection = next;
		// UX safety: when switching tabs, start list at the top so it doesn't feel like a "continued" view.
		queueMicrotask(() => {
			document.querySelector<HTMLElement>('.lessons')?.scrollTo({ top: 0 });
		});
	}

	$: lessons =
		activeSection === 'available'
			? plannedLessons
			: activeSection === 'enrolled'
				? enrolledLessons
				: activeSection === 'finished'
					? finishedLessons
					: [];
</script>

<div class="dashboard">
	{#if loading}
		<div class="app-loading" role="status" aria-live="polite" aria-busy="true">
			<div class="app-loading__content">
				<div class="app-loading__spinner" aria-hidden="true" />
				<div class="app-loading__label">Loading lessons…</div>
			</div>
		</div>
	{:else}
	<div class="options">
		<div class="title">
			<p class="hello">Hello,</p>
			{activeUser}
		</div>
		<div class="sections">
			{#each sections as section}
				<Button
					prefixIcon={section.icon}
					type={section.type ? section.type : (activeSection === section.key ? 'active' : 'inactive')}
					on:click={section.type === 'sign-out' ? handleSignOut : () => !section.type && setActiveSection(section.key)}
				>
					{section.label}
				</Button>
			{/each}
		</div>
	</div>
	{#key activeSection}
		<div class="lessons">
			{#if lessons.length > 0}
				{#each lessons as lesson (`${activeSection}:${lesson.id}`)}
					<Lesson
						currentUser={clientId}
						date={lesson.date}
						duration={lesson.duration}
						id={lesson.id}
						enrolledClients={lesson.clientsCount}
						firstName={lesson.instructor.firstName}
						lastName={lesson.instructor.lastName}
						qualificationLevel={lesson.instructor.qualificationLevel}
						rating={lesson.instructor.rating}
						section={activeSection}
						clients={[]}
					/>
				{/each}
			{:else}
				<div class="no-content">
					<img src="src/assets/empty.svg" alt="No content" class="svg"/>
					<p>There isn&#39;t anything here yet.</p>
				</div>
			{/if}
		</div>
	{/key}
	{/if}
</div>

<style lang="scss">
  .dashboard {
    display: flex;
    flex-direction: row;
		gap: 2.5rem;
		max-width: 1200px;
		width: min(1200px, calc(100vw - 3rem));
  }

  .title {
    margin-bottom: 30px;
    padding: 5px;

    .hello {
      font-weight: 300;
    }
  }

  .options,
  .sections {
    padding: 2px;
    display: flex;
    flex-direction: column;
  }

  .sections {
    gap: 15px;
  }

  .lessons {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
		flex: 1;
		min-width: 0;
		max-width: 780px;
		padding: 0.5rem 0 0.5rem 1rem;
    border-left: 1px solid #000;
		max-height: min(64vh, 640px);
    overflow-y: auto;
  }

	@media (max-width: 900px) {
		.dashboard {
			flex-direction: column;
			align-items: stretch;
			gap: 1.5rem;
		}

		.lessons {
			border-left: none;
			border-top: 1px solid #000;
			padding-left: 0;
			padding-top: 1rem;
			max-width: 100%;
		}
	}

	.no-content {
		display: flex;
		flex-direction: column;
		align-items: center;
    justify-content: center;
		gap: 20px;
		padding: 20px;
		text-align: center;

		p {
			font-weight: 300;
			color: #666;
		}
	}

	.svg{
		width: 150px;
		height: 150px;
	}

	/* loading styles are global in src/styles/_main.scss */
</style>