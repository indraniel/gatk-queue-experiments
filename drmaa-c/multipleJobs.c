/**************************************************************
 * Compile command
 * (on linus261)
 *     gcc -I /tmp/idas-local/include -L /tmp/idas-local/lib -ldrmaa -pthread -Wall -o drmaa-mult-job multipleJobs.c
 * (on blade)
 *     gcc -ldrmaa -thread -Wall -o drmaa-mult-job multipleJobs.c
**************************************************************/
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <getopt.h>
#include "drmaa.h"
#define MAX_LEN_JOBID 100

int submit_lsf_job(int);

int main(int argc, char *argv[]) {
    char diagnosis[DRMAA_ERROR_STRING_BUFFER];
    int i;

    if (drmaa_init(NULL, diagnosis, sizeof(diagnosis)-1) != DRMAA_ERRNO_SUCCESS) {
        fprintf(stderr, "drmaa_init() failed: %s\n", diagnosis);
        return 1;
    }

    for (i = 0; i < 2; i++) {
        submit_lsf_job(i);
    }

    return 0;
}

int submit_lsf_job(int submit_number) {
    char diagnosis[DRMAA_ERROR_STRING_BUFFER];
    int drmaa_errno = 0;
    char job_id[MAX_LEN_JOBID];
    char output_path[128] = { '\0' };
    char hello_world_id_buf[5] = { '\0' };
    const char *job_path = "echo";
    const char *job_argv[4] = {"Hello", "World", hello_world_id_buf, NULL};
    drmaa_job_template_t *jt = NULL;
    char *job_name    = NULL;
    const char* job_native = "-q short";
    char job_id_out[MAX_LEN_JOBID] = { '\0' };

    sprintf(hello_world_id_buf, "%d\n", submit_number);
    sprintf(output_path, ":%s/drmma-test-%d.out", DRMAA_PLACEHOLDER_HD, submit_number);

    memset(job_id, 0, sizeof(job_id));
    memset(job_id_out, 0, sizeof(job_id_out));

    if (drmaa_allocate_job_template(&jt, NULL, 0)!=DRMAA_ERRNO_SUCCESS) {
   	    fprintf(stderr, "drmaa_allocate_job_template() failed: %s\n", diagnosis);
        return 1;
    }

    if(job_name)
        drmaa_set_attribute(jt, DRMAA_JOB_NAME, job_name, NULL, 0);

    /* run in users home directory */
    drmaa_set_attribute(jt, DRMAA_WD, DRMAA_PLACEHOLDER_HD, NULL, 0);

    /* the job to be run */
    drmaa_set_attribute(jt, DRMAA_REMOTE_COMMAND, job_path, NULL, 0);

    /* the job's arguments */
    drmaa_set_vector_attribute(jt, DRMAA_V_ARGV, job_argv, NULL, 0);

    /* join output/error file */
    drmaa_set_attribute(jt, DRMAA_JOIN_FILES, "y", NULL, 0);  
   
    /* path for output */
    drmaa_set_attribute(jt, DRMAA_OUTPUT_PATH, output_path, NULL, 0);
    
    /* the job's native specification */
   drmaa_set_attribute(jt, DRMAA_NATIVE_SPECIFICATION, job_native, NULL, 0);

    /*run a job*/
    drmaa_errno=drmaa_run_job(job_id, 
                              sizeof(job_id)-1, jt, diagnosis,
							  sizeof(diagnosis)-1);
    
    if (drmaa_errno != DRMAA_ERRNO_SUCCESS) 
	{
	    fprintf(stderr, "drmaa_run_job() failed: %s\n", diagnosis);
	    return 1;
    }

    drmaa_delete_job_template(jt, NULL, 0);

    printf("Submitted job<%s> ...\n", job_id);
    
    return 0;
}


