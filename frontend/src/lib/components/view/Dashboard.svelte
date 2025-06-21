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
		<Lesson />
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
    height: 500px;
    padding: 10px 0 10px 10px;
    border-left: 1px solid #000;
  }
</style>