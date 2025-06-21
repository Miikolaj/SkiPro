<script lang="ts">
	import { Button, Lesson } from '$components';
	import {
		faPersonSkiing,
		faList,
		faFlagCheckered,
		faRepeat,
		faArrowRightFromBracket
	} from '@fortawesome/free-solid-svg-icons';

	let fullname: string = 'John Doe';
	let activeSection: string = 'available';

	const sections = [
		{ key: 'available', icon: faList, label: 'Available Lessons' },
		{ key: 'enrolled', icon: faPersonSkiing, label: 'Enrolled Lessons' },
		{ key: 'finished', icon: faFlagCheckered, label: 'Finished Lessons' },
		{ key: 'rentals', icon: faRepeat, label: 'Rentals' },
		{ key: 'signout', icon: faArrowRightFromBracket, label: 'Sign Out', type: 'sign-out' }
	];

	const lessonsBySection: Record<string, any[]> = {
		available: [
			{
				date: '2025-01-12 10:00',
				duration: '1h 30min',
				lessonNumber: '101',
				enrolledClients: '2',
				instructorName: 'Anna Nowak',
				qualificationLevel: 'Advanced',
				rating: '4.8'
			},
			{
				date: '2025-01-15 14:00',
				duration: '2h 0min',
				lessonNumber: '102',
				enrolledClients: '1',
				instructorName: 'Piotr Zielinski',
				qualificationLevel: 'Intermediate',
				rating: '4.5'
			}
		],
		enrolled: [
			{
				date: '2025-01-18 09:00',
				duration: '1h 0min',
				lessonNumber: '201',
				enrolledClients: '3',
				instructorName: 'Jan Kowalski',
				qualificationLevel: 'Beginner',
				rating: '4.7'
			}
		],
		finished: [
			{
				date: '2024-12-20 11:00',
				duration: '1h 30min',
				lessonNumber: '301',
				enrolledClients: '5',
				instructorName: 'Anna Nowak',
				qualificationLevel: 'Advanced',
				rating: '4.9'
			}
		]
	};

	$: lessons = lessonsBySection[activeSection] || [];
</script>

<div class="dashboard">
	<div class="options">
		<div class="title">
			<p class="hello">Hello,</p>
			{fullname}
		</div>
		<div class="sections">
			{#each sections as section}
				<Button
					prefixIcon={section.icon}
					type={section.type ? section.type : (activeSection === section.key ? 'active' : 'inactive')}
					on:click={() => !section.type && (activeSection = section.key)}
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
					lessonNumber={lesson.lessonNumber}
					enrolledClients={lesson.enrolledClients}
					instructorName={lesson.instructorName}
					qualificationLevel={lesson.qualificationLevel}
					rating={lesson.rating}
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