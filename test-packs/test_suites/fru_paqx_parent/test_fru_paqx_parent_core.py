import pytest
import af_support_tools

try:
    config_file = 'fru_paqx_parent/core_config.ini'
    env_file = 'env.ini'
except:
    print('Possible Configuration Error')

@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
@pytest.mark.parametrize("container_name,expected", [
	("symphony-credential-service",'Up'), 
	("symphony-identity-service",'Up'),
	("symphony-hal-orchestrator-service",'Up'),
	("symphony-system-definition-service",'Up'),
	("symphony-fru-paqx",'Up'),
	("symphony-capability-registry-service",'Up'),
	("symphony-endpoint-registration-service", 'Up'),
	("symphony-rackhd-adapter-service",'Up'),	
	("symphony-coprhd-adapter-service",'Up'),
	("symphony-vcenter-adapter-service",'Up'),
])

def test_generic_container_up(container_name, expected):
    """
    Description	:	This method asserts if the output returned from container_verify matches the expected string.
    Parameters	:	1. container_name	-	Name of the container(STRING)
    				2. expected			-	expected output to be obtained from container_verify function(STRING).
    Returns		: 	None
    """
    print (container_name + ' status after deployment')
    assert container_verify(container_name, 'Status', '1') == expected

def container_verify(container, attrib, pos):
    """
    Description	:	This method verifies if the container name is up or not.
    Parameters	:	1. container	-	Name of the container (STRING)
    				2. expected		-	expected output to be obtained from container_verify function(STRING).
    				3. pos			-	position of the string to be extracted from the formatted 'docker ps' command(STRING)
    Returns		: 	output			-	"Up\n" or "Down\n" string coming from 'docker ps' command(STRING).
    """
    print('Getting Values Specified Container')

    ipaddress = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='hostname')
    username = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='username')
    password = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='password')

    cmd = 'docker ps -f name=' + str(container) + ' --format "{{.' + str(attrib) + '}}" | awk \'{print $' + str(pos) + '}\''
    output = af_support_tools.send_ssh_command(host=ipaddress, username=username, password=password, command=cmd, return_output=True).strip()

    return output
