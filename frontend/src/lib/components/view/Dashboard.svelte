<script lang="ts">
	import { onMount } from 'svelte';
	import { Button, Lesson } from '$components';
	import { faPersonSkiing, faList, faFlagCheckered, faRepeat, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
	import { LessonRepository } from '$lib/repositories/lesson.repository';

	const lessonRepository = new LessonRepository();

	let loading = true;
	let activeSection: string = 'available';
	let enrolledLessons: any[] = [];
	let plannedLessons: any[] = [];
	let finishedLessons: any[] = [];
	export let clientId: string;
	export let activeUser: string;

	function normalizeLessons(lessons: any[]): any[] {
		return lessons.map(lesson => ({
			...lesson,
			clients: Array.isArray(lesson.clients) ? lesson.clients : []
		}));
	}

	onMount(async () => {
		enrolledLessons = normalizeLessons(await lessonRepository.getLessonsForClient(clientId));
		plannedLessons = normalizeLessons(await lessonRepository.getLessons(clientId));
		finishedLessons = normalizeLessons(await lessonRepository.getFinishedLessons(clientId));
		console.log(enrolledLessons);
		console.log(plannedLessons);
		console.log(finishedLessons);
		loading = false;
	});

	const sections = [
		{ key: 'available', icon: faList, label: 'Available Lessons' },
		{ key: 'enrolled', icon: faPersonSkiing, label: 'Enrolled Lessons' },
		{ key: 'finished', icon: faFlagCheckered, label: 'Finished Lessons' },
		{ key: 'rentals', icon: faRepeat, label: 'Rentals' },
		{ key: 'signout', icon: faArrowRightFromBracket, label: 'Sign Out', type: 'sign-out' }
	];

	const lessonsBySection: Record<string, any[]> = {
		available: [],
		enrolled: [],
		finished: []
	};

	function handleSignOut() {
		document.cookie = 'token=; Max-Age=0; path=/;';
		window.location.reload();
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
		<div class="loading">Loading...</div>
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
					on:click={section.type === 'sign-out' ? handleSignOut : () => !section.type && (activeSection = section.key)}
				>
					{section.label}
				</Button>
			{/each}
		</div>
	</div>
	<div class="lessons">
		{#if lessons.length > 0}
			{#each lessons as lesson}
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
					clients={lesson.clients}
				/>
			{/each}
		{:else}
			<div class="no-content">
				<img src="src/assets/empty.svg" alt="No content" class="svg"/>
				<p>There isn&#39;t anything here yet.</p>
			</div>
		{/if}
	</div>
	{/if}
</div>

<style lang="scss">
  .dashboard {
    display: flex;
    flex-direction: row;
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
    gap: 5px;
    max-width: 600px;
    min-width: 550px;
    height: 500px;
    padding: 10px 0 10px 10px;
    border-left: 1px solid #000;
    overflow-y: auto;
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
</style>