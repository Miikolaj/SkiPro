<script lang="ts">
	import { onMount } from 'svelte';
	import { Button, Lesson } from '$components';
	import { faPersonSkiing, faList, faFlagCheckered, faRepeat, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
	import { LessonRepository } from '$lib/repositories/lesson.repository';

	let activeSection: string = 'available';
	export let clientId: string;
	export let activeUser: string;

	const lessonRepository = new LessonRepository();

	let lessonsFromApi = [];
	onMount(async () => {
		lessonsFromApi = await lessonRepository.getLessonsForClient(clientId);
		console.log(lessonsFromApi);
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

	$: lessonsBySection.available = [...lessonsFromApi];
	$: lessons = lessonsBySection[activeSection] || [];
</script>

<div class="dashboard">
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
					date={lesson.date}
					duration={lesson.duration}
					id={lesson.id}
					enrolledClients={lesson.clients.length}
					firstName={lesson.instructor.firstName}
					lastName={lesson.instructor.lastName}
					qualificationLevel={lesson.instructor.qualificationLevel}
					rating={lesson.instructor.rating}
				/>
			{/each}
		{:else}
			<div class="no-content">
				<p>No lessons to di splay.</p>
			</div>
		{/if}
	</div>
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
  }
</style>