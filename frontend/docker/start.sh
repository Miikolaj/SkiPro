#!/bin/bash
id -u worker &>/dev/null || useradd -m worker
supervisord
