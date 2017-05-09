import pytest
import af_support_tools
import traceback
import requests
import executable_present

try:
    env_file = 'env.ini'
    ipaddress = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='hostname')
    user = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='username')
    password = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='password')


except Exception as e:
    print('Possible configuration error.')
    traceback.print_exc()
    raise Exception(e)


@pytest.mark.cli_chk
def test_fru_cli_chk():
    # Arrange
    file = "workflow-cli"

    # Act/Assert
    assert executable_present.executable_present(program=file), "CLI Not Present"


@pytest.mark.cli_version
def test_fru_cli_version():
    # Arrange
    command = './workflow-cli version'

    # Act
    response = af_support_tools.send_ssh_command(host=ipaddress,
                                                 username=user,
                                                 password=password,
                                                 command=command,
                                                 return_output=True)

    # Assert
    assert 'hi' in str(response)


@pytest.mark.api
def test_fru_api():
    # Arrange
    url = 'https://{}:8443/fru/api/about'.format(ipaddress)

    # Act
    response = requests.get(url, verify=False)
    print(url)

    # Assert
    assert response.status_code == 200
