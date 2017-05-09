#!/bin/bash
#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#

echo "Removing Dell Inc. FRU PAQX components"

systemctl disable fru-paqx

echo "Dell Inc. FRU PAQX components removal has completed successfully."

exit 0
