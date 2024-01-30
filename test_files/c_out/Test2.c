#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <stdbool.h>
#define MAXCHAR 512
char* integer_to_str(int i){
int length= snprintf(NULL,0,"%d",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%d",i);
return result;
}
char* real_to_str(float i){
int length= snprintf(NULL,0,"%f",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%f",i);
return result;
}
char* char_to_str(char i){
int length= snprintf(NULL,0,"%c",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%c",i);
return result;
}
char* bool_to_str(bool i){
int length= snprintf(NULL,0,"%d",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%d",i);
return result;
}
char* str_concat(char* str1, char* str2){
char* result=malloc(sizeof(char)*MAXCHAR);
result=strcat(result,str1);
result=strcat(result,str2);
return result;}

char* read_str(){
char* str=malloc(sizeof(char)*MAXCHAR);
scanf("%s",str);
return str;}

int str_to_bool(char* expr){
int i=0;
if ( (strcmp(expr, "true")==0) || (strcmp(expr, "1"))==0 )
i=1;
if ( (strcmp(expr, "false")==0) || (strcmp(expr, "0"))==0 )
i=0;
return i;}
void main(int argc, char *argv[]) {
float risultato=0.0;
char* ans=strdup("no");
char* taglia = (char *)malloc(100 * sizeof(char));
char* ans1 = (char *)malloc(100 * sizeof(char));
int x=3;
int a=1;
float b=2.2;
char* valore=strdup("nok");
{
int a=1;
int b=2;
while (a>b) {
printf("uno \n");
a = a-1;
}
while (a<b) {
printf("due \n");
a = a+1;
}
while (a==b) {
printf("3 \n");
a = a+2;
}
while (a!=b) {
printf("4 \n");
a = a+3;
}
while (a==b) {
a = a+5;
}
printf(" non sono uguali ");
}
}
