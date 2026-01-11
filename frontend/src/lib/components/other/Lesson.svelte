<script lang="ts">
	import { Button } from '$lib/components';
	import { faStopwatch, faCalendarDays, faStar } from '@fortawesome/free-solid-svg-icons';
	import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
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
	let expanded = false;

	let clientsLoading = false;
	let clientsError: string | null = null;
	let actionLoading = false;
	let actionError: string | null = null;

	// Safety: if this component instance ever gets reused for a different lesson/section,
	// don't keep UI state from the previous render.
	$: if (id || section) {
		expanded = false;
		clientsLoading = false;
		clientsError = null;
		actionLoading = false;
		actionError = null;
	}

	// simple in-memory cache across Lesson components
	const clientsCache: Map<string, { firstName: string; lastName: string; id: string }[]> = new Map();

	async function toggleExpanded() {
		expanded = !expanded;
		if (!expanded) return;

		// if already provided (non-empty) or cached, don't refetch
		if ((clients && clients.length > 0) || clientsCache.has(id)) {
			clients = clientsCache.get(id) ?? clients;
			return;
		}

		clientsLoading = true;
		clientsError = null;
		try {
			clients = await lessonRepository.getLessonClients(id);
			clientsCache.set(id, clients);
		} catch (e: unknown) {
			clientsError = e instanceof Error ? e.message : typeof e === 'string' ? e : 'Failed to load clients';
		} finally {
			clientsLoading = false;
		}
	}

	function onActionClick(event: Event) {
		// Don't toggle the card when clicking the action button.
		event.stopPropagation();
		if (actionLoading) return;
		if (uniqueClients.length === 5 && section === 'available') return;
		(section === 'available' ? handleEnroll : section === 'enrolled' ? handleCancel : handleEnroll)();
	}

	async function refreshPageNoCache() {
		// Ensure we don't re-use any cached load/SSR result.
		const url = new URL(window.location.href);
		url.searchParams.set('_ts', Date.now().toString());
		window.location.href = url.toString();
	}

	async function handleEnroll() {
		actionLoading = true;
		actionError = null;
		try {
			await lessonRepository.enrollLesson(id, currentUser);
			clientsCache.delete(id);
			localStorage.setItem('showSuccessModal', id.slice(-3));
			await refreshPageNoCache();
		} catch (e: unknown) {
			actionError = e instanceof Error ? e.message : typeof e === 'string' ? e : 'Failed to enroll';
		} finally {
			actionLoading = false;
		}
	}

	async function handleCancel() {
		actionLoading = true;
		actionError = null;
		try {
			await lessonRepository.cancelEnrollment(id, currentUser);
			clientsCache.delete(id);
			localStorage.setItem('showCancelModal', id.slice(-3));
			await refreshPageNoCache();
		} catch (e: unknown) {
			actionError = e instanceof Error ? e.message : typeof e === 'string' ? e : 'Failed to cancel enrollment';
		} finally {
			actionLoading = false;
		}
	}

	$: uniqueClients = Array.from(new Map(clients.map(c => [c.id, c])).values());
	$: progress = `${enrolledClients}/${maxClients}`;
	$: isFull = uniqueClients.length === Number(maxClients);
	$: sectionClass = section === 'available' ? 'available' : section === 'enrolled' ? 'enrolled' : section === 'finished' ? 'finished' : 'default';
	$: buttonLabel =
		section === 'available'
			? actionLoading
				? 'Enrolling...'
				: 'Enroll'
			: section === 'enrolled'
				? actionLoading
					? 'Canceling...'
					: 'Cancel Participation'
				: section === 'finished'
					? 'Rate Instructor'
					: 'Action';
</script>

<div
	class="lesson-card {sectionClass}{expanded ? ' expanded' : ''}"
	on:click={toggleExpanded}
	on:keydown={(e) => e.key === 'Enter' && toggleExpanded()}
	role="button"
	tabindex="0"
>
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
			<div class="instructor-wrapper">
				<div class="instructor-line">
					<span class="label">Instructor:</span>
					<span class="value">{firstName} {lastName}</span>
				</div>
				{#if expanded}
					<div class="qualification">Qualification : {qualificationLevel}</div>
					<div class="rating">Rating: {rating}/5
						<Fa icon={faStar} />
					</div>
				{/if}
				<div class="spacer" />
			</div>
			{#if expanded}
				<div class="enroll-wrapper">
					<Button
						disabled={(actionLoading || (isFull && section === 'available')).toString()}
						type={`lesson-tile${section === 'enrolled' ? ' inactive' : ''}${(isFull && section==='available')? ' dimmed' : ''}`}
						on:click={onActionClick}
					>
						{buttonLabel}
					</Button>
					{#if actionError}
						<div class="client">{actionError}</div>
					{/if}
				</div>
			{/if}
		</div>

		<div class="right-bottom">
			<div class="count">
				<span class="left">Enrolled Clients: </span>
				<span class="right">{progress}</span>
			</div>
			{#if expanded}
				{#if clientsLoading}
					<div class="client">Loading clients...</div>
				{:else if clientsError}
					<div class="client">{clientsError}</div>
				{:else}
					{#if uniqueClients.length > 0}
						{#each uniqueClients as c}
							<div class="client{c.id === currentUser ? ' highlighted' : ''}">{c.firstName} {c.lastName}</div>
						{/each}
					{/if}
				{/if}
			{/if}
		</div>
	</div>
	<div class="click-hint bottom">
		<span class="hint-text">{expanded ? 'Hide details' : 'Click to show details'}</span>
		<span class="chevron {expanded ? 'open' : ''}" aria-hidden="true">
			<Fa icon={faChevronDown} size="0.9x" />
		</span>
	</div>
</div>

<style lang="scss">
  .lesson-card {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    padding: 15px;
		margin-right: 20px;
    border: 1px solid #000;
    border-radius: 5px;
    font-weight: 300;
		cursor: pointer;
		background: #fff;
		transition: transform 120ms ease, box-shadow 120ms ease, background-color 120ms ease;
  }

	.lesson-card:hover {
		transform: translateY(-1px);
		box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
		background: #fafcff;
	}

	.lesson-card.expanded {
		background: #f7fbff;
	}

	.lesson-card.available {
		border-left: 4px solid #1976d2;
	}

	.lesson-card.enrolled {
		border-left: 4px solid #2e7d32;
	}

	.lesson-card.finished {
		border-left: 4px solid #6d4c41;
	}

  .client.highlighted {
    font-weight: bold;
    color: #1976d2;
  }

  .top-section {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 0;

    .lesson-title {
      font-size: 1rem;
      font-weight: 700;
      color: #0f172a;
      margin: 0;
    }

    .lesson-description {
      display: flex;
      gap: 15px;
    }

    .icons {
      display: flex;
      align-items: center;
      gap: 5px;
			color: #475569;
			font-weight: 400;
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
      gap: 10px;
      min-width: 145px;
      font-size: 0.875rem;

      .spacer {
        flex: 1;
      }

      .count {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 100%;
			align-items: center;
			gap: 10px;

			.left {
				color: #334155;
				font-weight: 600;
			}
			.right {
				font-weight: 800;
				color: #0f172a;
				background: rgba(25, 118, 210, 0.10);
				border: 1px solid rgba(25, 118, 210, 0.25);
				padding: 2px 8px;
				border-radius: 999px;
			}
      }
    }

    .left-bottom {
      display: flex;
      flex-direction: column;
      height: 100%;
    }

    .enroll-wrapper {
      margin-top: auto;
      display: flex;
      justify-content: flex-start;
    }

    .instructor-wrapper {
      display: flex;
      flex-direction: column;
      gap: 10px;
      font-size: 0.875rem;
			color: #334155;

			.label {
				color: #475569;
				font-weight: 500;
				margin-right: 6px;
			}
			.value {
				color: #0f172a;
				font-weight: 800;
			}

			.qualification,
			.rating {
				color: #334155;
				font-weight: 500;
			}
    }
  }

	.click-hint {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 10px;
		margin-top: -10px;
		color: #5f6b7a;
		font-size: 0.8rem;
		user-select: none;
		font-weight: 600;
	}

	.click-hint.bottom {
		margin-top: 0;
		padding-top: 10px;
		border-top: 1px solid rgba(0, 0, 0, 0.08);
	}

	.hint-text {
		opacity: 0.9;
	}

	.chevron {
		display: inline-flex;
		transition: transform 160ms ease;
		transform: rotate(0deg);
	}
	.chevron.open {
		transform: rotate(180deg);
	}
</style>
