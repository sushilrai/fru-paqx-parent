import json
import pytest
import re
import requests
from requests.models import PreparedRequest
import requests.exceptions

@pytest.fixture(scope='module')
def fru_paqx_url():
    protocol = 'https://'
    host = '10.3.249.204'
    port = '18443'
    endpoint = '/fru/api/workflow/'
    URL = protocol + host + ':' + port + endpoint
    return URL

@pytest.fixture(scope='module')
def fru_paqx_headers():
    with open('fixtures/headers.json') as fixture:
        headers = json.loads(fixture.read())
        return headers

@pytest.fixture(scope='module')
def get_workflow_id(fru_paqx_url, fru_paqx_headers):
    with open('fixtures/start_quanta_workflow.json') as fixture:
        requestBody = json.loads(fixture.read())
        r = requests.post(fru_paqx_url, json=requestBody, headers=fru_paqx_headers, verify =False)
        data = r.json()
        return data['id']

'''
Private helper for UUID matching
'''
def _create_uuid_pattern():
    return re.compile(
        (
            '[a-f0-9]{8}-' +
            '[a-f0-9]{4}-' +
            '[1-5]' + '[a-f0-9]{3}-' +
            '[89ab][a-f0-9]{3}-' +
            '[a-f0-9]{12}$'
        ),
        re.IGNORECASE
    )

'''
Private helper to validate URLs, lifted from Django
'''
def _check_url():
    return re.compile(
        r'^(?:http|ftp)s?://' # http:// or https://
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|' #domain...
        r'localhost|' #localhost...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?' # optional port
        r'(?:/?|[/?]\S+)$', 
    re.IGNORECASE)


'''
Validate that:
  -  all fields match expected values
'''

@pytest.mark.api
def test_start_quanta_workflow(fru_paqx_url, fru_paqx_headers):
    with open('fixtures/start_quanta_workflow.json') as fixture:
        # Arrange
        requestBody = json.loads(fixture.read())
        # Act
        r = requests.post(fru_paqx_url, json=requestBody, headers=fru_paqx_headers, verify=False)
        data = r.json()
        # Assert
        assert _create_uuid_pattern().match(data['id'])
        assert data['workflow'] == 'quanta-replacement-d51b-esxi'
        assert data['currentStep'] == 'captureRackHDEndpoint'
        assert data['currentStepNumber'] == -1
        assert data['expectedNumberOfSteps'] == -1 
        assert data['links'][0]['rel'] == 'step-next'
        assert _check_url().match(data['links'][0]['href'])
        assert data['links'][0]['type'] == 'application/vnd.dellemc.rackhd.endpoint+json'
        assert data['links'][0]['method'] == 'POST'

'''
Add assertions here
'''
@pytest.mark.api
def test_list_active_jobs(fru_paqx_url):
    headers = {'Content-Type': 'application/json'}
    r = requests.get(fru_paqx_url, headers=headers, verify =False)
    print(r.headers)

'''
Validate that:
  -  id passed in URL matches the id returned in body
'''
@pytest.mark.current
@pytest.mark.api
def test_get_job_details(fru_paqx_url, get_workflow_id, fru_paqx_headers):
    r = requests.get(fru_paqx_url + get_workflow_id, headers=fru_paqx_headers, verify=False)
    data = r.json()
    assert data['id'] == get_workflow_id


