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
</script>


<div class="dashboard">
	<div class="options">
		<div class="title">
			<p class="hello">Hello,</p>
			{fullname}
		</div>
		<div class="sections">
			<Button prefixIcon={faList} type="active">Available Lessons</Button>
			<Button prefixIcon={faPersonSkiing} type="inactive">Enrolled Lessons</Button>
			<Button prefixIcon={faFlagCheckered} type="inactive">Finished Lessons</Button>
			<Button prefixIcon={faRepeat} type="inactive">Rentals</Button>
			<Button prefixIcon={faArrowRightFromBracket} type="sign-out">Sign Out</Button>
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